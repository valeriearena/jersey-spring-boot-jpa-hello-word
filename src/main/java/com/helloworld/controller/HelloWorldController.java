package com.helloworld.controller;

import com.helloworld.properties.HelloWorldProperties;
import com.helloworld.representation.HelloWorldGreetingJaxb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/controller/helloworld")
public class HelloWorldController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldController.class);

	@Value("${logging.level.org.springframework.web}")
	private String loggingLevelWeb;

	@Value("${logging.level.org.springframework.boot}")
	private String loggingLevelBoot;

	@Value("${helloworld.name}")
	private String helloWorldName;

	@Value("${helloworld.description}")
	private String helloWorldDescription;

	@Autowired
	private HelloWorldProperties helloWorldProperties;

	@Autowired
	private CounterService counter;

	private static List<HelloWorldGreetingJaxb> greetings = new ArrayList<HelloWorldGreetingJaxb>();

	static  {
		greetings.add(new HelloWorldGreetingJaxb("Hello to Spring Boot","SpringBoot", "Val"));
		greetings.add(new HelloWorldGreetingJaxb("Hello to Spring 4","Spring4", "Val"));
		greetings.add(new HelloWorldGreetingJaxb("Hello to Everyone","Everyone", "Val"));
		greetings.add(new HelloWorldGreetingJaxb("Hello World","World", "Val"));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String rest(){

		counter.increment("counter.index.invoked");

		LOGGER.info("************** loggingLevelWeb={}",loggingLevelWeb);
		LOGGER.info("************** loggingLevelBoot={}",loggingLevelBoot);

		LOGGER.info("************** helloWorldName={}",helloWorldProperties.getName());
		LOGGER.info("************** helloWorldDescription={}",helloWorldProperties.getDescription());

		return "Hello World!!!";
	}

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<HelloWorldGreetingJaxb> getAll(){
		return greetings;
	}


	@RequestMapping(value = "findBy/from/{from}", method = RequestMethod.GET)
	public List<HelloWorldGreetingJaxb> findByFrom(@PathVariable String from){
		return greetings
				.stream()
				.filter(greeting -> greeting.getFrom().equalsIgnoreCase(from))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/findBy/to/{to}", method = RequestMethod.GET)
	public List<HelloWorldGreetingJaxb> findByTo(@PathVariable String to){
		return greetings
				.stream()
				.filter(greeting -> greeting.getTo().equalsIgnoreCase(to))
				.collect(Collectors.toList());
	}

	public static List<HelloWorldGreetingJaxb> getGreetings() {
		return greetings;
	}
}
