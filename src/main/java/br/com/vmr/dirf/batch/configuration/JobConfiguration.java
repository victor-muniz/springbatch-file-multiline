/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.vmr.dirf.batch.configuration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import br.com.vmr.dirf.batch.domain.DIRF;
import br.com.vmr.dirf.batch.domain.DIRFDetail;
import br.com.vmr.dirf.batch.domain.DetailFieldSetMapper;
import br.com.vmr.dirf.batch.domain.HeaderFieldSetMapper;
import br.com.vmr.dirf.batch.listener.ProtocolListener;
import br.com.vmr.dirf.batch.reader.DIRFItemReader;

/**
 * @author Victor Rosa
 */

@Configuration
public class JobConfiguration {
	
	@Autowired
	private ResourcePatternResolver resourcePatternResolver;

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public DelimitedLineTokenizer headerTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter("#|");
		tokenizer.setNames(new String[] { "lineID", "merchantID", "merchantName" });
		return tokenizer;
	}

	@Bean
	public DelimitedLineTokenizer detailTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter("#|");
		tokenizer.setNames(new String[] { "lineID", "cnpj", "january", "february", "march", "april", "may", "june",
				"july", "august", "september", "october", "november", "december" });
		return tokenizer;
	}

	@Bean
	public DelimitedLineTokenizer footerTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "lineID" });
		return tokenizer;
	}

	@Bean
	public PatternMatchingCompositeLineTokenizer lineTokenizer() {
		PatternMatchingCompositeLineTokenizer pattern = new PatternMatchingCompositeLineTokenizer();
		Map<String, LineTokenizer> tokenizers = new HashMap<String, LineTokenizer>();
		tokenizers.put("HEADER*", headerTokenizer());
		tokenizers.put("DETAIL*", detailTokenizer());
		tokenizers.put("FOOTER*", footerTokenizer());
		pattern.setTokenizers(tokenizers);
		return pattern;
	}

	
	@Bean
	public DefaultLineMapper<FieldSet> lineMapper() {
		DefaultLineMapper<FieldSet> defaultLineMapper = new DefaultLineMapper<FieldSet>();
		defaultLineMapper.setLineTokenizer(lineTokenizer());
		defaultLineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());
		defaultLineMapper.afterPropertiesSet();
		return defaultLineMapper;
	}


	@Bean
	@StepScope
	public FlatFileItemReader<FieldSet> fileItemReader(
			@Value("#{stepExecutionContext[fileName]}") Resource resource) {
		FlatFileItemReader<FieldSet> itemReader = new FlatFileItemReader<FieldSet>();
		itemReader.setResource(resource);
		itemReader.setLineMapper(lineMapper());	
		return itemReader;
	}
	
	
	@Bean
	@StepScope
	public DIRFItemReader reader() {
		DIRFItemReader reader = new DIRFItemReader();
		reader.setFieldSetReader(fileItemReader(null));
		reader.setHeaderMapper(new HeaderFieldSetMapper());
		reader.setDetailMapper(new DetailFieldSetMapper());
		return reader;
	}
	

	@Bean
	@StepScope
	public ItemWriter<DIRF> writer() {
		return items -> {

			for (DIRF item : items) {
				BufferedWriter writer = new BufferedWriter(
						new FileWriter("C:/tmp/" + item.getMerchantID() + "-" + item.hashCode() + ".txt"));
				writer.write(item.toString());
				for (DIRFDetail detail : item.getDetails()) {
					writer.newLine();
					writer.write(detail.toString());
				}
				writer.close();
			}
		};
	}

	
	@Bean
	public Partitioner partitioner(){
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources("classpath:/data/*.txt");
		} catch (IOException e) {
			throw new RuntimeException("I/O problems when resolving the input file pattern.",e);
		}
		partitioner.setResources(resources);
		return partitioner;
	}
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setCorePoolSize(6);
		taskExecutor.setQueueCapacity(3);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}
	
	
	@Bean
	public Step masterStep() throws Exception {
		return stepBuilderFactory.get("step1")
				.partitioner(slaveStep().getName(), partitioner())
				.step(slaveStep())
				.gridSize(4)
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public Step slaveStep() {
		return stepBuilderFactory.get("slaveStep")
				.<DIRF, DIRF>chunk(100)
				.reader(reader())
				.writer(writer())
				.stream(fileItemReader(null))
				.build();
	}

	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("job")
				.start(masterStep())
				.listener(protocolListener())
				.build();
	}
	
	@Bean
	public ProtocolListener protocolListener(){
		return new ProtocolListener(taskExecutor());
	}

}
