package com.vendingmachine.resttemplate;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vendingmachine.backend.entity.AppUser;
import com.vendingmachine.backend.vo.JSGridReturnData;

//import reactor.core.publisher.Mono;

//@Component
public class RestTemplateUtil {

	@Autowired
	@Qualifier("defauletRestTemplate")//對應RestTemplateConfig的name
	private RestTemplate restTemplate;
	
	
	public Object get() {
		return null;
	}
	
	public JSGridReturnData<AppUser> test() throws JsonProcessingException {
		JSGridReturnData<AppUser> resp = postOfType("http://localhost:8080/test","",new ParameterizedTypeReference<JSGridReturnData<AppUser>>() {});
		return resp;
	}
	
	public <T> T postOfType(String url, Object request, ParameterizedTypeReference<T> respType) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(mapper), headers);
		
		ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, respType);

	    return response.getBody();
		//return restTemplate.exchange(url, HttpMethod.POST, entity, respType);
	}
	
	public <T> T post(String url, Object request, Class<T> respClass) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(mapper), headers);
		
		return restTemplate.postForObject(url, entity, respClass);
	}
	
	/*public <T> T monoPost(String url, Object request, Class<T> respClass) throws JsonProcessingException, URISyntaxException {
		WebClient client = WebClient.create();
		MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
		bodyValues.add("key", "value");
		bodyValues.add("another-key", "another-value");

		T response = client.post()
		    .uri(new URI("https://httpbin.org/post"))
		    .header("Authorization", "Bearer MY_SECRET_TOKEN")
		    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
		    .accept(MediaType.APPLICATION_JSON)
		    .bodyValue(request)
		    //.body(BodyInserters.fromFormData(bodyValues))
		    .retrieve()
		    .bodyToMono(respClass)
		    .block();

		return response;
	}*/
}
