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

public class DIRFDetail {
	
	private String cnpj;
	private String januaryAmount;
	private String februaryAmount;
	private String marchAmount;
	private String aprilAmount;
	private String mayAmount; 
	private String juneAmount;
	private String julyAmount;
	private String augustAmount;
	private String septemberAmount;
	private String octoberAmount;
	private String novemberAmount;
	private String decemberAmount;

	public DIRFDetail(String cnpj, String januaryAmount, String februaryAmount, String marchAmount,
			String aprilAmount, String mayAmount, String juneAmount, String julyAmount, String augustAmount,
			String septemberAmount, String octoberAmount, String novemberAmount, String decemberAmount) {
		super();
		this.cnpj = cnpj;
		this.januaryAmount = januaryAmount;
		this.februaryAmount = februaryAmount;
		this.marchAmount = marchAmount;
		this.aprilAmount = aprilAmount;
		this.mayAmount = mayAmount;
		this.juneAmount = juneAmount;
		this.julyAmount = julyAmount;
		this.augustAmount = augustAmount;
		this.septemberAmount = septemberAmount;
		this.octoberAmount = octoberAmount;
		this.novemberAmount = novemberAmount;
		this.decemberAmount = decemberAmount;
	}

	public String getCnpj() {
		return cnpj;
	}

	@Override
	public String toString() {
		return "DIRFDetail{" +
				"CNPJ='" + cnpj + '\'' +
				", januaryAmount='" + januaryAmount + '\'' +
				", februaryAmount='" + februaryAmount + '\'' +
				", marchAmount=" + marchAmount + '\'' +
				", aprilAmount=" + aprilAmount + '\'' +
				", mayAmount=" + mayAmount + '\'' +
				", juneAmount=" + juneAmount + '\'' +
				", julyAmount=" + julyAmount + '\'' +
				", augustAmount=" + augustAmount + '\'' +
				", septemberAmount=" + septemberAmount + '\'' +
				", octoberAmount=" + octoberAmount + '\'' +
				", novemberAmount=" + novemberAmount + '\'' +
				", decemberAmount=" + decemberAmount +
				'}';
	}

}
