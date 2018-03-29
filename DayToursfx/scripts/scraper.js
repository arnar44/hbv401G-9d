const fs = require('fs');
const util = require('util');
const writeFileAsync = util.promisify(fs.writeFile);

const output = 'scripts/test.txt';
const encoding = 'utf8';

async function write(filepath, content) {
  return writeFileAsync(filepath, content, { encoding });
}

async function main() {
	console.log('start');
	
  await write(output, "plís virkaðu");
  console.log(`Done writing ${output}`)
}

main();