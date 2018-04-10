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
	

	if(!tourTitle) {
		return false;
	}

	const infoBox = $('div.row div.large-10 div.row div.large-8 div.row.tour__tabs div div.tabs-content div.tabs-panel.is-active div.row.tour__icons');

	const tourDescription = $('div.row div.large-10 div.row div.large-8 div.row div.large-9');

	// meet on location or depart from rvk?
	const departFrom = $(infoBox).find('ul[data-toggle=dropdown-departs_from] li.icon-text p strong').text();

	const info = {
		title: tourTitle,
		category: tourCatagory,
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
		category,
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
		category,
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
	const resolvedFetch = [];

	let tmp;
	let text;

	await Promise.all((links).map(async (category) => {
		tmp = await fetch(category.link);
		text = await tmp.text();
		resolvedFetch.push({html: text, category: category.title});
	}));
	
	
	for (const obj of resolvedFetch) {

		const $ = cheerio.load(obj.html);
		const a = $('div.row div.large-10.medium-9.medium-push-3.large-push-2.columns div.row.bottom-margin div a');

		a.each((i, link) => {
			const href = $(link).attr('href');
			tourLinks.push({ link: href, category: obj.category });
		});
	}

	return tourLinks;
}

async function makeToursFromCategories(categories) {
	const tours = [];
	const resolvedFetch = [];

	const round1 = categories.slice(0 * categories.length / 6, 1 * categories.length / 6);
	const round2 = categories.slice(1 * categories.length / 6, 2 * categories.length / 6);
	const round3 = categories.slice(2 * categories.length / 6, 3 * categories.length / 6);
	const round4 = categories.slice(3 * categories.length / 6, 4 * categories.length / 6);
	const round5 = categories.slice(4 * categories.length / 6, 5 * categories.length / 6);
	const round6 = categories.slice(5 * categories.length / 6, 6 * categories.length / 6);

	// Fleiri svona === minna load á server === minni líkur á að tapa gögnum (erum að tapa ca 15 - 25 núna all good)
	await mapHelper(round1, resolvedFetch);
	await mapHelper(round2, resolvedFetch);
	await mapHelper(round3, resolvedFetch);
	await mapHelper(round4, resolvedFetch);
	await mapHelper(round5, resolvedFetch);
	await mapHelper(round6, resolvedFetch);
	
	for (const element of resolvedFetch) {
		const $ = cheerio.load(element.html);
		const tour = maketour($, element.category);
		
		if (tour) {
			tours.push(tour);
		}
	}
	
	return tours;
}

async function mapHelper(array, returnArray) {
	let tmp;
	let text;

	await Promise.all((array).map(async (category) => {
		tmp = await fetch(category.link);
		text = await tmp.text();
		returnArray.push({html: text, category: category.category});
	}));
}

async function main() {
	console.log('running scraper');
	
	// sækja forsíðu
	const $ = await fetchBase();
	
	// sækja alla categories og linka á þeirra síðu
	const linkCategories = getCategories($);
	console.log('1.stage done, reading categories');
	
	
	// sækja allar ferðir af catagory síðu
	const categories = await fetchCategoryTours(linkCategories);
	console.log('2.stage done, getting tours from categories');
	

	// búa til json með uppl um allar ferðir
	const toursCategories = await makeToursFromCategories(categories);
	console.log('Trips to be added ',toursCategories.length);
	
	
	
	console.log('3.stage done, inserting to database');

	// setja i gagnagrunn
	const db = new sqlite3.Database('src/database/DayTours.db');
	db.serialize(function() {
		const str = 'insert into tours (title, category, availability, duration, level, pickup, meet, departures, price, description) values (?,?,?,?,?,?,?,?,?,?)'
		const stmt = db.prepare(str);

		toursCategories.forEach(dayTour => {
			const {
				title,
				category,
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
					category,
					availability, 
					duration, 
					level, 
					pickup, 
					meet, 
					departures, 
					price,
					description], 
				function(err, row) {
					if(err) {
						console.error(err);
					}
				});
		});
	});

	console.log('database loaded and will close');
}

main();