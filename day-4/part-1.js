const fs = require('fs');

const result = fs.readFileSync("./test", "utf-8")
    .split(/\r?\n/)
    .map(line => {
        if(line.trim() == "") {
            return false;
        }
        
        [raw1, raw2] = line.trim().split(",");
        
        const range1 = toRange(raw1);
        const range2 = toRange(raw2);

        const intersection = range1.filter(a=>range2.includes(a));
        const isOverlapped = intersection.length == range1.length || intersection.length == range2.length;
        
        return isOverlapped;
    })
    .filter(result=>result)

console.log(result.length);

function toRange(raw) {
    [lower, upper] = raw.split("-").map(item=>parseInt(item))
    return [...Array(upper-lower+1).keys()].map(i=>i+lower)
}