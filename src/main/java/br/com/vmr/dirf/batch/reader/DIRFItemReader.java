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
package br.com.vmr.dirf.batch.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import br.com.vmr.dirf.batch.domain.DIRF;
import br.com.vmr.dirf.batch.domain.DIRFDetail;

public class DIRFItemReader implements ItemReader<DIRF> {
	
	private static final Log logger = LogFactory.getLog(DIRFItemReader.class);

	private DIRF dirf;

	private boolean recordFinished;

	private FieldSetMapper<DIRF> headerMapper;

	private FieldSetMapper<DIRFDetail> detailMapper;

	private ItemReader<FieldSet> fieldSetReader;
	
	@Override
	public DIRF read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
	recordFinished = false;

		while (!recordFinished) {
				process(fieldSetReader.read());
		}
		DIRF result = dirf;
		dirf = null;
		return result;
	}	


	
	private void process(FieldSet fieldSet) throws Exception {
	
		if (fieldSet == null) {
			logger.debug("FINISHED");
			recordFinished = true;
			dirf = null;
			return;
		}

		String lineId = fieldSet.readString(0);

		if (DIRF.HEADER.equals(lineId)) {
			logger.debug("STARTING NEW DIRF RECORD (HEADER): " + fieldSet.toString());
			dirf = headerMapper.mapFieldSet(fieldSet);
		}
		else if (DIRF.FOOTER.equals(lineId)) {
			logger.debug("END OF DIRF RECORD (FOOTER): " + fieldSet.toString());
			recordFinished = true;
		}
		else if (DIRF.DETAIL.equals(lineId)) {
			logger.debug("ADDING DIRF DETAIL RECORD: " + fieldSet.toString() );
			if (dirf != null) {
				dirf.addDetail(detailMapper.mapFieldSet(fieldSet));
			}else {
				throw new Exception ("HEADER NOT FOUND FOR DETAIL: " +  fieldSet.toString()); 
			} 
		
		}
		else {
			logger.debug("Could not map LINE_ID=" + lineId);
		}
	}

	public void setHeaderMapper(FieldSetMapper<DIRF> headerMapper) {
		this.headerMapper = headerMapper;
	}

	public void setDetailMapper(FieldSetMapper<DIRFDetail> detailMapper) {
		this.detailMapper = detailMapper;
	}
	public void setFieldSetReader(ItemReader<FieldSet> fieldSetReader) {
		this.fieldSetReader = fieldSetReader;
	}




}
