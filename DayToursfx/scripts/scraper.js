require('isomorphic-fetch');
const cheerio = require('cheerio');

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
	console.log(basicInfo.text());
	
	
	

/* 	basicInfo.each((i, info) => {
		console.log(info);
		console.log('t');
		
	}); */
}

async function fetchTours(links) {
	console.log(links);
	
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
	
	const $ = await fetchBase();
	
	links = getLinks($);
	fetchTours(links);
	
	console.log('finito');
	
}

main();