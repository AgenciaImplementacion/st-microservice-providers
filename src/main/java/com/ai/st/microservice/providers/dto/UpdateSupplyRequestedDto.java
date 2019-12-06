package com.ai.st.microservice.providers.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "UpdateSupplyRequestedDto", description = "Update Supply Requested Dto")
public class UpdateSupplyRequestedDto implements Serializable {

	private static final long serialVersionUID = -1302858811309199472L;

	@ApiModelProperty(required = false, notes = "Delivered")
	private Boolean delivered;

	@ApiModelProperty(required = false, notes = "Justification")
	private String justification;

	public UpdateSupplyRequestedDto() {

	}

	public Boolean getDelivered() {
		return delivered;
	}

	public void setDelivered(Boolean delivered) {
		this.delivered = delivered;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

}
