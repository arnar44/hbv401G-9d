require('isomorphic-fetch');
const cheerio = require('cheerio');
const sqlite3 = require('sqlite3').verbose();
const path = require("path");

const output = 'scripts/test.txt';
const encoding = 'utf8';
const baseurl = 'https://adventures.is';

async function fetchBase() {
	const response = await fetch(`${baseurl}/iceland/day-tours/`);
	const html = await response.text();

	const $ = cheerio.load(html);

	return $
}

function getCategories($) {
	const a = $('div.row.large-up-5.medium-up-4.small-up-2.cat div.column a');

	const links = [];

	a.each((i, link) => {
		const href = $(link).attr('href');
		const h4 = $(link).find('h4').text();

		links.push({link: href, title: h4 });
	});	
	
	 return links
}

function maketour($, tourCatagory) {
	const tourTitle = $('section.hero div.orbit div.row div div h1').text();
	const infoBox = $('div.row div.large-10 div.row div.large-8 div.row.tour__tabs div div.tabs-content div.tabs-panel.is-active div.row.tour__icons');

	const tourDescription = $('div.row div.large-10 div.row div.large-8 div.row div.large-9');

	// meet on location or depart from rvk?
	const departFrom = $(infoBox).find('ul[data-toggle=dropdown-departs_from] li.icon-text p strong').text();

	const info = {
		title: tourTitle,
		catagory: tourCatagory,
		availability: $(infoBox).find('ul[data-toggle=dropdown-availability] li.icon-text p strong').text(),
		duration: $(infoBox).find('ul[data-toggle=dropdown-duration] li.icon-text p strong').text(),
		level: $(infoBox).find('ul[data-toggle=dropdown-level] li.icon-text p strong span').text(),
		pickup: $(infoBox).find('ul[data-toggle=dropdown-pickup] li.icon-text p strong').text(),
		meet: $(infoBox).find('ul[data-toggle=dropdown-meet_on_location] li.icon-text p strong').text(),
		departures: departFrom ? departFrom : 'Reykjavík',
		price: $(infoBox).find('ul[data-toggle=dropdown-price] li.icon-text p strong').text().replace(/[^0-9]/, ''),
		description: $(tourDescription).find('h2').text().replace(/\s+/g, " ").trim()
	}

	const {
		title,
		catagory,
		availability= '', 
		duration= '', 
		level= '', 
		pickup= '', 
		meet= '', 
		departures='',
		price= '',
		description= ''
	} = info;

	
	 return {
		title,
		catagory,
		availability, 
		duration, 
		level, 
		pickup, 
		meet, 
		departures, 
		price,
		description
	};
	
	

}

async function fetchCategoryTours(links) {
	
	const tourLinks = [];

	// Breyta þessu í fylki af promises og nota promise.all.then... fyrir betri tima
	for (const category of links) {
		const response = await fetch(category.link);
		const html = await response.text();
		
		const $ = cheerio.load(html);
		const a = $('div.row div.large-10.medium-9.medium-push-3.large-push-2.columns div.row.bottom-margin div a');

		a.each((i, link) => {
			const href = $(link).attr('href');
			tourLinks.push({ link: href, category: category.title });
		});
	}

	return tourLinks;
}

async function makeToursFromCategories(categories) {
	const tours = [];

	// breyta þessu í fylki af promises og nota promise.all.then... fyrir betri tima
	for (const element of categories) {
		const response = await fetch(element.link);
		const html = await response.text();
		
		const $ = cheerio.load(html);

		tours.push(maketour($, element.category))
	}
	return tours;
	
}

async function main() {
	console.log('running scraper');
	
	// sækja forsíðu
	const $ = await fetchBase();
	
	// sækja alla categories og linka á þeirra síðu
	const linkCategories = getCategories($);
	console.log('1.stage done');
	
	
	// sækja allar ferðir af catagory síðu
	const categories = await fetchCategoryTours(linkCategories);
	console.log('2.stage done');
	

	// búa til json með uppl um allar ferðir
	const toursCategories = await makeToursFromCategories(categories);
	console.log('3.stage done');

	// setja i gagnagrunn
	const db = new sqlite3.Database('src/database/DayTours.db');
	db.serialize(function() {
		const str = 'insert into tours (title, category, availability, duration, level, pickup, meet, departures, price, description) values (?,?,?,?,?,?,?,?,?,?)'
		const stmt = db.prepare(str);

		toursCategories.forEach(dayTour => {
			const {
				title,
				catagory,
				availability, 
				duration, 
				level, 
				pickup, 
				meet, 
				departures, 
				price,
				description
			} = dayTour;

				stmt.run(
					[title,
					catagory,
					availability, 
					duration, 
					level, 
					pickup, 
					meet, 
					departures, 
					price,
					description], 
				function(err, row) {
					console.error('row already exists');
				});
		});
	});
	
	db.close();
	
}

main();