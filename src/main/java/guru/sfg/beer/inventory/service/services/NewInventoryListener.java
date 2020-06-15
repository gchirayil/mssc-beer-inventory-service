package guru.sfg.beer.inventory.service.services;

import javax.transaction.Transactional;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.common.events.BeerDto;
import guru.sfg.common.events.NewInventoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewInventoryListener {
	
	private final BeerInventoryRepository beerInventoryRepository;
	
    @Transactional
	@JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
	public void listen(NewInventoryEvent event) {
    	//NewInventoryEvent newInventoryEvent = (NewInventoryEvent) event;
		BeerDto beerDto = event.getBeerDto();
		log.debug("Listener on queue "+JmsConfig.NEW_INVENTORY_QUEUE+" retrieved: "+beerDto);
		
		beerInventoryRepository.save(BeerInventory.builder()
				.beerId(beerDto.getId())
				.upc(beerDto.getUpc())
				.quantityOnHand(beerDto.getQuantityOnHand())
				.build());
    }

}
