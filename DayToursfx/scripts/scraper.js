require('isomorphic-fetch');
const cheerio = require('cheerio');
const sqlite3 = require('sqlite3').verbose();

const util = require('util');

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

function maketour($) {
	
	const basicInfo = $('#sidebar .sidebar .box');

 	basicInfo.each((i, info) => {
		{
			// spurning hvort ég scrapi stærri bita og minnka hann her
		}
	});
}

async function fetchTours(links) {
	
	const tours = [];

	links.forEach(async (url) => {
		const response = await fetch(`${baseurl}${url}`);
		const html = await response.text();
		
		const $ = cheerio.load(html);
		

		tours.push(maketour($));

	});
}

async function main() {
	console.log('start');

/* 	
	const db = new sqlite3.Database('DayTours.db');
	db.serialize(function() {
		var stmt = db.prepare('insert into tours (title, location, duration, difficulty, iternirary) values (?,?,?,?,?)');
		stmt.run('test2', '500', 'test2', 'test2', 'test2', 'test2');
		db.each("SELECT * FROM tours", function(err, row) {
			console.log(row);
	 });
	});
 */
	
	
	const $ = await fetchBase();
	
	links = getLinks($);
	fetchTours(links);
	
	console.log('finito');
}

main();