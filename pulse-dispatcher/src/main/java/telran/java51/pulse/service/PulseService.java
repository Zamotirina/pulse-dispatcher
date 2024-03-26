package telran.java51.pulse.service;

import java.util.function.Consumer;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import telran.java51.pulse.dto.PulseDto;

@Configuration
@RequiredArgsConstructor

public class PulseService {

	final StreamBridge streamBridge;

	@Value(value = "${min}")
	int minPulse;
	@Value(value = "${max}")
	int maxPulse;
	
	@Bean
	Consumer <PulseDto> dispatchPulse() {
		
		return data -> {
			
			if(data.getPayload()<minPulse) {	
				streamBridge.send("lowpulse-out-0",data);
				return;
			}
			
			if(data.getPayload()>maxPulse) {	
				streamBridge.send("highpulse-out-0",data);
				return;
			}
			
			long delay = System.currentTimeMillis() - data.getTimeStamp();
			System.out.println("delay: "+delay+", id: "+data.getId()+", pulse: "+data.getPayload());
	
		
		};
	}
}
