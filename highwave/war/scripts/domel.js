/*
	domEl() function - painless DOM manipulation
	written by Pawel Knapik  //  pawel.saikko.com
*/
var domEl = function(e,c,a,p,x) {
if(e||c) {
	c=(typeof c=='string'||(typeof c=='object'&&!c.length))?[c]:c;	
	e=(!e&&c.length==1)?document.createTextNode(c[0]):e;	
	var n = (typeof e=='string')?document.createElement(e) : !(e&&e===c[0])?e.cloneNode(false):e.cloneNode(true);	
	if(e.nodeType!=3) {
		c[0]===e?c[0]='':'';
		for(var i=0,j=c.length;i<j;i++) {
			if(typeof c[i]=='string' && c[i].length!=0) n.appendChild(document.createTextNode(c[i]));
			else if(c[i].length!=0) n.appendChild(c[i].cloneNode(true));
		}
		if(a) {for(var i=(a.length-1);i>=0;i--) a[i][0]=='class'?n.className=a[i][1]:n.setAttribute(a[i][0],a[i][1]);}
	}
}
	if(!p)return n;
	p=(typeof p=='object'&&!p.length)?[p]:p;
	for(var i=(p.length-1);i>=0;i--) {
		if(x){while(p[i].firstChild)p[i].removeChild(p[i].firstChild);
			if(!e&&!c&&p[i].parentNode)p[i].parentNode.removeChild(p[i]);}
		if(n) p[i].appendChild(n.cloneNode(true));
	}	
}

/*
	getElementsByClass - algorithm by Dustin Diaz, shortened by Pawel Knapik
*/
function getElementsByClass(s,n,t) {
	var c=[], e=(n?n:document).getElementsByTagName(t?t:'*'),r=new RegExp("(^|\\s)"+s+"(\\s|$)");
	for (var i=0,j=e.length;i<j;i++) r.test(e[i].className)?c.push(e[i]):''; return c }
	
/*
	$() based on prototype.js dollar function idea, optimized by Pawel Knapik.
*/
function $(){var r=[],a=arguments;for(var i=0,j=a.length;i<j;i++){(typeof a[i]=='string')?(r.push(document.getElementById(a[i]))):(r.push(a[i]))}
return(r.length==1)?r[0]:r}