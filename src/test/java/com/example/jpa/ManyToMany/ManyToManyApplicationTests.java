package com.example.jpa.ManyToMany;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ManyToManyApplicationTests {
	@Autowired
	BookRepository bookRepo;
	@Autowired
	StoreRepository storeRepo;
	@Autowired
	EntityManager em;

	void flushAndClear() {
		em.flush();
		em.clear();
	}

	@Test
	void shouldAddBook() {
		bookRepo.save( new Book( "untitled", "no author", LocalDate.parse("2015-02-20") ) );
		flushAndClear();
		Book b = bookRepo.findByTitle("untitled").get( 0 );
		assertThat( b.getAuthor() ).isEqualTo( "no author" );
	}

	@Test
	void shouldAddStore() {
		LocalTime time = LocalTime.parse("10:30");
		ZoneOffset offset = ZoneOffset.of("+08:00");
		LocalTime time2 = LocalTime.parse("22:30");
		ZoneOffset offset2 = ZoneOffset.of("+08:00");
		storeRepo.save( new Store("ABC books", "11 Bourke Street", OffsetTime.of( time, offset ), OffsetTime.of(time2, offset2) ) );
		flushAndClear();
		Store s = storeRepo.findByName("ABC books").get( 0 );
		assertThat( s.getAddress() ).isEqualTo( "11 Bourke Street" );
	}

	@Test
	void shouldGetBooksBelongingToStore() {
		LocalTime time = LocalTime.parse("10:30");
		ZoneOffset offset = ZoneOffset.of("+08:00");
		LocalTime time2 = LocalTime.parse("22:30");
		ZoneOffset offset2 = ZoneOffset.of("+08:00");
		Store s = new Store("ABC books", "11 Bourke Street", OffsetTime.of( time, offset ), OffsetTime.of(time2, offset2) );
		Book b1 = new Book( "untitled", "no author", LocalDate.parse("2015-02-20") );
		Book b2 = new Book( "lean startup", "Eric", LocalDate.parse("2011-09-29") );
		s.addBook( b1 );
		s.addBook( b2 );
		storeRepo.save( s );
		flushAndClear();
		Store s2 = storeRepo.findByName("ABC books").get( 0 );
		assertThat( s2.getBooks().size() ).isEqualTo( 2 );
	}

	@Test
	void shouldGetStoresSellingThisBook() {
		LocalTime time = LocalTime.parse("10:30");
		ZoneOffset offset = ZoneOffset.of("+08:00");
		LocalTime time2 = LocalTime.parse("22:30");
		ZoneOffset offset2 = ZoneOffset.of("+08:00");
		Store s = new Store("ABC books", "11 Bourke Street", OffsetTime.of( time, offset ), OffsetTime.of(time2, offset2) );
		Book b1 = new Book( "untitled", "no author", LocalDate.parse("2015-02-20") );
		Book b2 = new Book( "lean startup", "Eric", LocalDate.parse("2011-09-29") );
		s.addBook( b1 );
		s.addBook( b2 );
		storeRepo.save( s );
		flushAndClear();
		Book b = bookRepo.findByTitle("untitled").get( 0 );
		Store s1 = b.getStores().get( 0 );
		assertThat( s1.getName() ).isEqualTo( "ABC books" );
	}

	@Test
	void shouldGetStoreOperationalDuration() {
		LocalTime time = LocalTime.parse("10:30");
		ZoneOffset singapore = ZoneOffset.of("+08:00");
		LocalTime time2 = LocalTime.parse("22:30");
		Store s = new Store("ABC books", "11 Bourke Street", OffsetTime.of( time, singapore ), OffsetTime.of(time2, singapore) );
		Long duration = Duration.between( s.getOpeningTime(singapore), s.getClosingTime(singapore) ).getSeconds();
		assertThat( duration / 60 / 60 ).isEqualTo( 12 );
	}

	@Test
	void shouldConvertOperatingHoursToSpecificTimeZone() {
		LocalTime time = LocalTime.parse("10:30");
		ZoneOffset singapore = ZoneOffset.of("+08:00");
		LocalTime time2 = LocalTime.parse("22:30");
		ZoneOffset indonesia = ZoneOffset.of("+07:00");
		Store s = new Store("ABC books", "11 Bourke Street", OffsetTime.of( time, singapore ), OffsetTime.of(time2, singapore) );
		OffsetTime adjustedTime = s.getOpeningTime(indonesia);
		assertThat( adjustedTime.format(ISO_LOCAL_TIME) ).isEqualTo("09:30:00");
	}
}
