package com.ai.st.microservice.providers.controllers.v1;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.providers.business.ProviderBusiness;
import com.ai.st.microservice.providers.dto.CreateProviderDto;
import com.ai.st.microservice.providers.dto.ErrorDto;
import com.ai.st.microservice.providers.dto.ProviderDto;
import com.ai.st.microservice.providers.dto.ProviderUserDto;
import com.ai.st.microservice.providers.dto.RequestDto;
import com.ai.st.microservice.providers.dto.TypeSupplyDto;
import com.ai.st.microservice.providers.exceptions.BusinessException;
import com.ai.st.microservice.providers.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Manage Providers", description = "Manage Providers", tags = { "Providers" })
@RestController
@RequestMapping("api/providers-supplies/v1/providers")
public class ProviderV1Controller {

	private final Logger log = LoggerFactory.getLogger(ProviderV1Controller.class);

	@Autowired
	private ProviderBusiness providerBusiness;

	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get providers")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get providers", response = ProviderDto.class, responseContainer = "List"),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<List<ProviderDto>> getProviders() {

		HttpStatus httpStatus = null;
		List<ProviderDto> listProviders = new ArrayList<ProviderDto>();

		try {

			listProviders = providerBusiness.getProviders();

			httpStatus = HttpStatus.OK;
		} catch (BusinessException e) {
			listProviders = null;
			log.error("Error ProviderV1Controller@getProviders#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			listProviders = null;
			log.error("Error ProviderV1Controller@getProviders#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(listProviders, httpStatus);
	}

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create provider")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Create provider", response = ProviderDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<ProviderDto> createProvider(@RequestBody CreateProviderDto createProviderDto) {

		HttpStatus httpStatus = null;
		ProviderDto responseProviderDto = null;

		try {

			// validation input data
			if (createProviderDto.getName().isEmpty()) {
				throw new InputValidationException("The provider name is required.");
			}
			if (createProviderDto.getTaxIdentificationNumber().isEmpty()) {
				throw new InputValidationException("The tax identification number is required.");
			}
			if (createProviderDto.getProviderCategoryId() == null) {
				throw new InputValidationException("The provider category is required.");
			}

			responseProviderDto = providerBusiness.createProvider(createProviderDto.getName(),
					createProviderDto.getTaxIdentificationNumber(), createProviderDto.getProviderCategoryId());

			httpStatus = HttpStatus.CREATED;
		} catch (InputValidationException e) {
			log.error("Error ProviderV1Controller@createProvider#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
		} catch (BusinessException e) {
			log.error("Error ProviderV1Controller@createProvider#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ProviderV1Controller@createProvider#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(responseProviderDto, httpStatus);
	}

	@RequestMapping(value = "/{providerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get provider by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get provider by id", response = ProviderDto.class),
			@ApiResponse(code = 404, message = "Provider does not exists.", response = ProviderDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<ProviderDto> getProviderById(
			@PathVariable(name = "providerId", required = true) Long providerId) {

		HttpStatus httpStatus = null;
		ProviderDto providerDto = null;

		try {
			providerDto = providerBusiness.getProviderById(providerId);
			httpStatus = (providerDto instanceof ProviderDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (BusinessException e) {
			log.error("Error ProviderV1Controller@getProviderById#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error ProviderV1Controller@getProviderById#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(providerDto, httpStatus);
	}

	@RequestMapping(value = "/{providerId}/types-supplies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get types supplies by provider")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get types supplies by provider", response = ProviderDto.class),
			@ApiResponse(code = 404, message = "Provider not found.", response = ProviderDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> getTypeSuppliesByProvider(@PathVariable Long providerId) {

		HttpStatus httpStatus = null;
		List<TypeSupplyDto> listTypesSupplies = new ArrayList<TypeSupplyDto>();
		Object responseDto = null;

		try {

			listTypesSupplies = providerBusiness.getTypesSuppliesByProviderId(providerId);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ProviderV1Controller@getTypeSuppliesByProvider#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ProviderV1Controller@getTypeSuppliesByProvider#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return (responseDto != null) ? new ResponseEntity<>(responseDto, httpStatus)
				: new ResponseEntity<>(listTypesSupplies, httpStatus);
	}

	@RequestMapping(value = "/{providerId}/requests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get requests by provider")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get requests by provider", response = ProviderDto.class),
			@ApiResponse(code = 404, message = "Provider not found.", response = ProviderDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> getRequestsByProvider(@PathVariable Long providerId,
			@RequestParam(required = false, name = "state") Long requestStateId) {

		HttpStatus httpStatus = null;
		List<RequestDto> listRequests = new ArrayList<RequestDto>();
		Object responseDto = null;

		try {

			listRequests = providerBusiness.getRequestsByProviderAndState(providerId, requestStateId);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ProviderV1Controller@getRequestsByProvider#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ProviderV1Controller@getRequestsByProvider#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return (responseDto != null) ? new ResponseEntity<>(responseDto, httpStatus)
				: new ResponseEntity<>(listRequests, httpStatus);

	}
	
	@RequestMapping(value = "/{providerId}/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get users by provider")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Get users by provider", response = ProviderDto.class),
			@ApiResponse(code = 404, message = "Provider not found.", response = ProviderUserDto.class),
			@ApiResponse(code = 500, message = "Error Server", response = String.class) })
	@ResponseBody
	public ResponseEntity<Object> getUsersByProvider(@PathVariable Long providerId) {

		HttpStatus httpStatus = null;
		List<ProviderUserDto> listUsers = new ArrayList<ProviderUserDto>();
		Object responseDto = null;

		try {

			listUsers = providerBusiness.getUsersByProvider(providerId);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error ProviderV1Controller@getUsersByProvider#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error ProviderV1Controller@getUsersByProvider#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return (responseDto != null) ? new ResponseEntity<>(responseDto, httpStatus)
				: new ResponseEntity<>(listUsers, httpStatus);

	}

}
