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
package br.com.vmr.dirf.batch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DetailFieldSetMapper implements FieldSetMapper<DIRFDetail> {



	public DIRFDetail mapFieldSet(FieldSet fieldSet) throws BindException {
		return new DIRFDetail(fieldSet.readString("cnpj"),
				fieldSet.readString("january"),
				fieldSet.readString("february"),
				fieldSet.readString("march"),
				fieldSet.readString("april"),
				fieldSet.readString("may"),
				fieldSet.readString("june"),
				fieldSet.readString("july"),
				fieldSet.readString("august"),
				fieldSet.readString("september"),
				fieldSet.readString("october"),
				fieldSet.readString("november"),
				fieldSet.readString("december")
				);
	
	}

}
