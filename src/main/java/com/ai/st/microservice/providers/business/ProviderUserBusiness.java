package com.ai.st.microservice.providers.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.providers.dto.ProviderCategoryDto;
import com.ai.st.microservice.providers.dto.ProviderDto;
import com.ai.st.microservice.providers.entities.ProviderEntity;
import com.ai.st.microservice.providers.entities.ProviderUserEntity;
import com.ai.st.microservice.providers.exceptions.BusinessException;
import com.ai.st.microservice.providers.services.IProviderUserService;

@Component
public class ProviderUserBusiness {

	@Autowired
	private IProviderUserService providerUserService;

	public ProviderDto getProviderByUserCode(Long userCode) throws BusinessException {

		ProviderDto providerDto = null;

		List<ProviderUserEntity> listProvidersUsersEntity = providerUserService.getProvidersUsersByCodeUser(userCode);

		if (listProvidersUsersEntity.size() > 0) {
			ProviderUserEntity providerUserEntity = listProvidersUsersEntity.get(0);
			ProviderEntity providerEntity = providerUserEntity.getProvider();

			providerDto = new ProviderDto();
			providerDto.setId(providerEntity.getId());
			providerDto.setName(providerEntity.getName());
			providerDto.setCreatedAt(providerEntity.getCreatedAt());
			providerDto.setTaxIdentificationNumber(providerEntity.getTaxIdentificationNumber());
			providerDto.setProviderCategory(new ProviderCategoryDto(providerEntity.getProviderCategory().getId(),
					providerEntity.getProviderCategory().getName()));
		}

		return providerDto;
	}

}
