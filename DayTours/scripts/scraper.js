require('isomorphic-fetch');
const cheerio = require('cheerio');
const sqlite3 = require('sqlite3').verbose();
const path = require("path");

const output = 'scripts/test.txt';
const encoding = 'utf8';
const baseurl = 'https://www.igtours.is';

async function fetchBase() {
	const response = await fetch(`${baseurl}/en/tours`);
	const html = await response.text();

	const $ = cheerio.load(html);

	return $
}

function getLinks($) {
	const li = $('li h2 a');

	const links = [];

	li.each((i, link) => {
		const href = $(link).attr('href');
		links.push(href);
	});	

	return links
}

function randomLocation() {
	const locations = ['Reykjavík', 'Akureyri', 'Egilsstaðir'];
	return locations[Math.floor(Math.random() * locations.length)];
}

function maketour($) {
	const basicInfo = $('#sidebar .sidebar');
	
	return {
		title: $('h1').text().substring(10).trim(),
		price: $(basicInfo).find('.box.price .boxText').text(),
		location: randomLocation(),
		duration: $(basicInfo).find('.box.duration .boxText .duration').text(),
		difficulty: $(basicInfo).find('.box.duration .boxText .level').text(),
		departures: $(basicInfo).find('.box.departures .boxText').text().replace(/\s+/g, " ").trim(),
		description: $('.description').text(),
	}
}

async function fetchTours(links) {
	
	const tours = [];

	for (const url of links) {
		const response = await fetch(`${baseurl}${url}`);
		const html = await response.text();
		
		const $ = cheerio.load(html);
		
		tours.push(maketour($));
	}

	return tours;
	
}

async function main() {
	console.log('running scraper');
	const a = 'foo';
	console.log(a);
	

	
	
console.log(path.resolve("./"));
console.log(path.resolve(__dirname));
 	
	
	const $ = await fetchBase();
	
	links = getLinks($);
	const tours = await fetchTours(links);
	
	const db = new sqlite3.Database('src/database/DayTours.db');
	db.serialize(function() {
		const stmt = db.prepare('insert into tours (title, price, location, duration, difficulty, description) values (?,?,?,?,?,?)');

		tours.forEach(dayTour => {
			const {
				title,
				price,
				duration,
				difficulty,
				departures,
				description
			} = dayTour;

				stmt.run([title, price, duration, difficulty, departures, description], 
				function(err, row) {
					console.error('row already exists');
				});
		});
	});
	
	db.close();
}

main();