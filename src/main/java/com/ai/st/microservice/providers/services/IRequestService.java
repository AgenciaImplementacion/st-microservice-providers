package com.ai.st.microservice.providers.services;

import java.util.List;

import com.ai.st.microservice.providers.entities.RequestEntity;

public interface IRequestService {

	public RequestEntity createRequest(RequestEntity requestEntity);

	public List<RequestEntity> getRequestsByProviderIdAndStateId(Long providerId, Long requestStateId);

	public RequestEntity getRequestById(Long id);

	public RequestEntity updateRequest(RequestEntity requestEntity);

}
