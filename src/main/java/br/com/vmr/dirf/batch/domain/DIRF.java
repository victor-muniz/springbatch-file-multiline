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

import java.util.ArrayList;
import java.util.List;

public class DIRF {


	public static final String HEADER = "HEADER";
	public static final String DETAIL = "DETAIL";
	public static final String FOOTER = "FOOTER";
	
	private String merchantID;
	private String merchantName;
	private List<DIRFDetail> details = new ArrayList<DIRFDetail>();



	public DIRF(String merchantID, String merchantName) {
		super();
		this.merchantID = merchantID;
		this.merchantName = merchantName;
	}

	public String getMerchantID() {
		return merchantID;
	}

	

	public List<DIRFDetail> getDetails() {
		return details;
	}



	public void addDetail(DIRFDetail detail) {
		details.add(detail);
	}
	
	@Override
	public String toString() {
		return "DIRFMerchant{" +
				"merchantID='" + merchantID + '\'' +
				", merchantName='" + merchantName +
				'}';
	}


	



	

}
