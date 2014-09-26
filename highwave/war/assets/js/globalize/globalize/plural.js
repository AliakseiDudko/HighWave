/*!
 * Globalize v1.0.0-alpha.6 2014-09-01T21:32Z Released under the MIT license
 * http://git.io/TrdQbw
 */
!function(a,b){"function"==typeof define&&define.amd?define(["cldr","../globalize","cldr/event"],b):"object"==typeof exports?module.exports=b(require("cldrjs"),require("globalize")):b(a.Cldr,a.Globalize)}(this,function(a,b){var c=b._formatMessage,d=b._validate,e=b._validateCldr,f=b._validateDefaultLocale,g=b._validateParameterPresence,h=b._validateParameterType,i=b._validateParameterTypePlainObject,j=function(a,b,c){d("E_PAR_MISSING_KEY","Parameter `{name}` misses key `{key}`",void 0===a||c in a,{key:c,name:"messageData"})},k=function(a,b){h(a,b,void 0===a||"number"==typeof a,"Number")},l=function(){function a(a,b){function c(){}function d(a){return function(){var b,c;for(b=0;b<a.length;b++)if(c=a[b](),null!==c)return c;return null}}function e(a){var b,c,d=G,e=[];for(b=0;b<a.length;b++){if(c=a[b](),null===c)return G=d,null;e.push(c)}return e}function f(a,b){return function(){for(var c=G,d=[],e=b();null!==e;)d.push(e),e=b();return d.length<a?(G=c,null):d}}function g(b){var c=b.length;return function(){var d=null;return a.substr(G,c)===b&&(d=b,G+=c),d}}function h(b){return function(){var c=a.substr(G).match(b);return null===c?null:(G+=c[0].length,c[0])}}function i(){var a=K();return null===a?(c(" -- failed i",parseInt(b,10)),a):(a=parseInt(b,10),c(" -- passed i ",a),a)}function j(){var a=J();return null===a?(c(" -- failed n ",b),a):(a=parseFloat(b,10),c(" -- passed n ",a),a)}function k(){var a=L();return null===a?(c(" -- failed f ",b),a):(a=(b+".").split(".")[1]||0,c(" -- passed f ",a),a)}function l(){var a=M();return null===a?(c(" -- failed t ",b),a):(a=(b+".").split(".")[1].replace(/0$/,"")||0,c(" -- passed t ",a),a)}function m(){var a=N();return null===a?(c(" -- failed v ",b),a):(a=(b+".").split(".")[1].length||0,c(" -- passed v ",a),a)}function n(){var a=O();return null===a?(c(" -- failed w ",b),a):(a=(b+".").split(".")[1].replace(/0$/,"").length||0,c(" -- passed w ",a),a)}function o(){var a=e([C,H,d([T,U]),H,I]);return null===a?(c(" -- failed mod"),null):(c(" -- passed "+parseInt(a[0],10)+" "+a[2]+" "+parseInt(a[4],10)),parseInt(a[0],10)%parseInt(a[4],10))}function p(){var a=e([H,V]);return null===a?(c(" -- failed not"),null):a[1]}function q(){var a=e([D,H,d([P]),H,I]);return null!==a?(c(" -- passed is : "+a[0]+" == "+parseInt(a[4],10)),a[0]===parseInt(a[4],10)):(c(" -- failed is"),null)}function r(){var a=e([D,H,d([Q,R]),H,I]);return null!==a?(c(" -- passed isnot: "+a[0]+" != "+parseInt(a[4],10)),a[0]!==parseInt(a[4],10)):(c(" -- failed isnot"),null)}function s(){var a,b,d=e([D,H,R,H,t]);if(null!==d){for(c(" -- passed not_in: "+d[0]+" != "+d[4]),b=d[4],a=0;a<b.length;a++)if(parseInt(b[a],10)===parseInt(d[0],10))return!1;return!0}return c(" -- failed not_in"),null}function t(){var a=e([d([v,I]),f(0,u)]),b=[];return null!==a?(b=b.concat(a[0]),a[1][0]&&(b=b.concat(a[1][0])),b):(c(" -- failed rangeList"),null)}function u(){var a=e([Z,t]);return null!==a?a[1]:(c(" -- failed rangeTail"),null)}function v(){var a,b,d,f,g=e([I,Y,I]);if(null!==g){for(c(" -- passed range"),b=[],d=parseInt(g[0],10),f=parseInt(g[2],10),a=d;f>=a;a++)b.push(a);return b}return c(" -- failed range"),null}function w(){var a,b,g;if(a=e([D,f(0,p),H,d([W,S]),H,t]),null!==a){for(c(" -- passed _in:"+a),b=a[5],g=0;g<b.length;g++)if(parseInt(b[g],10)===parseInt(a[0],10))return"not"!==a[1][0];return"not"===a[1][0]}return c(" -- failed _in "),null}function x(){var a,b;return b=e([D,f(0,p),H,X,H,t]),null!==b?(c(" -- passed within"),a=b[5],b[0]>=parseInt(a[0],10)&&b[0]<parseInt(a[a.length-1],10)?"not"!==b[1][0]:"not"===b[1][0]):(c(" -- failed within "),null)}function y(){var a,b=e([E,f(0,z)]);if(b){if(!b[0])return!1;for(a=0;a<b[1].length;a++)if(!b[1][a])return!1;return!0}return c(" -- failed and"),null}function z(){var a=e([H,_,H,E]);return null!==a?(c(" -- passed andTail"+a),a[3]):(c(" -- failed andTail"),null)}function A(){var a=e([H,$,H,y]);return null!==a?(c(" -- passed orTail: "+a[3]),a[3]):(c(" -- failed orTail"),null)}function B(){var a,b=e([y,f(0,A)]);if(b){for(a=0;a<b[1].length;a++)if(b[1][a])return!0;return b[0]}return!1}if(a=a.split("@")[0].replace(/^\s*/,"").replace(/\s*$/,""),!a.length)return!0;var C,D,E,F,G=0,H=h(/^\s+/),I=h(/^\d+/),J=g("n"),K=g("i"),L=g("f"),M=g("t"),N=g("v"),O=g("w"),P=g("is"),Q=g("is not"),R=g("!="),S=g("="),T=g("mod"),U=g("%"),V=g("not"),W=g("in"),X=g("within"),Y=g(".."),Z=g(","),$=g("or"),_=g("and");if(c("pluralRuleParser",a,b),C=d([j,i,k,l,m,n]),D=d([o,C]),E=d([q,s,r,w,x]),F=B(),null===F)throw new Error("Parse error at position "+G.toString()+" for rule: "+a);return G!==a.length&&c("Warning: Rule not parsed completely. Parser stopped at "+a.substr(0,G)+" for rule: "+a),F}return a}(),m=function(a,b){var c,d=b.supplemental("plurals-type-cardinal/{language}");for(c in d)if(l(d[c],a))return c.replace(/pluralRule-count-/,"");return null};return b.formatPlural=b.prototype.formatPlural=function(a,b,d){var e;return g(a,"value"),g(b,"messageData"),i(b,"messageData"),h(d,"formatValue",void 0===d||"string"==typeof d||"number"==typeof d,"String or Number"),e=this.plural(a),d=void 0===d?a:d,j(b,"messageData",e),c(b[e],[d])},b.plural=b.prototype.plural=function(a){var b,c;return g(a,"value"),k(a,"value"),b=this.cldr,f(b),b.on("get",e),c=m(a,b),b.off("get",e),d("E_INVALID_CLDR","{description}","string"==typeof c,{description:"Missing rules to deduce plural form of `"+a+"`"}),c},b});