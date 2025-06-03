package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {
    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeersNoAttributes() {
        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void listBeersNoBeerName() {
        beerClient.listBeers(null, BeerStyle.ALE, true, 1, 10);
    }

    @Test
    void listBeers() {
        beerClient.listBeers("ALE", BeerStyle.ALE, true, 1, 10);
    }

    @Test
    void getBeerById() {
        Page<BeerDTO> beerDTOS = beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().get(0);

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void testCreateBeer() {
        BeerDTO dto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("423423432")
                .build();

        BeerDTO savedDto = beerClient.createBeer(dto);
        assertNotNull(savedDto);
    }

    @Test
    void testUpdateBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("423423432")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDto);

        final String newName = "Mango Bobs 3";

        beerDTO.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(beerDTO);
        assertEquals(newName, updatedBeer.getBeerName());
    }

    @Test
    void testDeleteBeer() {
        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("423423432")
                .build();

        BeerDTO beerDTO = beerClient.createBeer(newDto);

        beerClient.deleteBeer(beerDTO.getId());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beerDTO.getId());
        });
    }
}