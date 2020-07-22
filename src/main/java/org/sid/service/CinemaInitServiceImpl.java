package org.sid.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.dao.CinemaRepository;
import org.sid.dao.SalleRepository;
import org.sid.dao.VilleRepository;
import org.sid.dao.PlaceRepository;
import org.sid.dao.SeanceRepository;
import org.sid.dao.FilmRepository;
import org.sid.dao.ProjectionRepository;
import org.sid.dao.CategorieRepository;
import org.sid.dao.TicketRepository;
import org.sid.entities.Categorie;
import org.sid.entities.Cinema;
import org.sid.entities.Film;
import org.sid.entities.Place;
import org.sid.entities.Projection;
import org.sid.entities.Salle;
import org.sid.entities.Seance;
import org.sid.entities.Ticket;
import org.sid.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jayway.jsonpath.internal.function.text.Length;


@Service
@Transactional
public class CinemaInitServiceImpl implements ICinemaInitService {
    @Autowired
	private VilleRepository villeRepository;
    @Autowired
	private CinemaRepository cinemaRepository;
    @Autowired
	private SalleRepository salleRepository;
    @Autowired
	private PlaceRepository placeRepository;
    @Autowired
	private SeanceRepository seanceRepository;
    @Autowired
	private FilmRepository filmRepository;
    @Autowired
	private ProjectionRepository projectionRepository;
    @Autowired
	private CategorieRepository categorieRepository;
    @Autowired
	private TicketRepository ticketRepository;
	
	
	
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca","Marrakech","Rabat","Tanger").forEach(nameVille->{
		Ville ville=new Ville();
		ville.setName(nameVille);
		villeRepository.save(ville);
		});
		
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v->{
			Stream.of("MEGARAMA","IMAX","FOUNOUN","CHAHRAZAD","DAOULIZ").forEach(nameCinema->{
				Cinema cinema=new Cinema();
				cinema.setName(nameCinema);
				cinema.setNbSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
		
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema->{
			for(int i=0;i<cinema.getNbSalles();i++) {
				Salle salle=new Salle();
				salle.setName("Salle "+(i+1));
				salle.setCinema(cinema);
				salle.setNbPlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
		
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle->{
			for(int i=0;i<salle.getNbPlace();i++) {
				Place place=new Place();
				place.setNum(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
		
	}

	@Override
	public void initSeances() {
		DateFormat dateFormat=new SimpleDateFormat("HH:mm");
		Stream.of("12:00","15:00","17:00","19:00","21:00").forEach(s->{
			Seance seance=new Seance();
			try {
				seance.setHeureDebut(dateFormat.parse(s));
				seanceRepository.save(seance);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire","Action","Fiction","Drama").forEach(cat->{
			Categorie categorie=new Categorie();
			categorie.setName(cat);
			categorieRepository.save(categorie);
		});
		
	}

	@Override
	public void initFilms() {
		double[] durees=new double[] {1,2.5,2,2.5,3};
		List<Categorie> categories=categorieRepository.findAll();
		Stream.of("Joker","Spider Man","Harry Potter","Hunger Games","Fast and Furious","Cat Woman").forEach(titreFilm->{
			Film film=new Film();
			film.setTitre(titreFilm);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(titreFilm.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
			
			
		});
		
	}

	@Override
	public void initProjections() {
		double[] prices=new double[] {30,50,60,70,90,100};
		List<Film> films=filmRepository.findAll();
		villeRepository.findAll().forEach(ville->{
			ville.getCinemas().forEach(cinema->{
				cinema.getSalles().forEach(salle->{
					int index=new Random().nextInt(films.size());
					Film film=films.get(index);
		
						seanceRepository.findAll().forEach(seance->{
							Projection projection=new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
							
							
			
					});
				});
			});
		});
		
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(p->{
			p.getSalle().getPlaces().forEach(place->{
				Ticket ticket=new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(p.getPrix());
				ticket.setProjection(p);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
		
	}

}
