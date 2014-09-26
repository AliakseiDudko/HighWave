/*!
 * Globalize v1.0.0-alpha.6 2014-09-01T21:32Z Released under the MIT license
 * http://git.io/TrdQbw
 */
!function(a,b){"function"==typeof define&&define.amd?define(["cldr","cldr/event"],b):"object"==typeof exports?module.exports=b(require("cldrjs")):a.Globalize=b(a.Cldr)}(this,function(a){function b(a){a.once("get",i),a.get("supplemental/likelySubtags")}function c(a){return this instanceof c?(k(a,"locale"),n(a,"locale"),this.cldr=q(a),void b(this.cldr)):new c(a)}var d=function(a){return"string"==typeof a?a:"number"==typeof a?""+a:JSON.stringify(a)},e=function(a,b){return a=a.replace(/{[0-9a-zA-Z-_. ]+}/g,function(a){return a=a.replace(/^{([^}]*)}$/,"$1"),d(b[a])})},f=function(a,b,c){var d;return b=a+(b?": "+e(b,c):""),d=new Error(b),d.code=a,Object.keys(c).forEach(function(a){d[a]=c[a]}),d},g=function(a,b,c,d){if(!c)throw f(a,b,d)},h=function(a){return Array.isArray(a)?a:a?[a]:[]},i=function(a,b,c){var d;c=c||{},d=h(c.skip).some(function(b){return b.test(a)}),g("E_MISSING_CLDR","Missing required CLDR content `{path}`.",b||d,{path:a})},j=function(a){g("E_DEFAULT_LOCALE_NOT_DEFINED","Default locale has not been defined.",void 0!==a,{})},k=function(a,b){g("E_MISSING_PARAMETER","Missing required parameter `{name}`.",void 0!==a,{name:b})},l=function(a,b,c,d){g("E_PAR_OUT_OF_RANGE","Parameter `{name}` has value `{value}` out of range [{minimum}, {maximum}].",void 0===a||a>=c&&d>=a,{maximum:d,minimum:c,name:b,value:a})},m=function(a,b,c,d){g("E_INVALID_PAR_TYPE","Invalid `{name}` parameter ({value}). {expected} expected.",c,{expected:d,name:b,value:a})},n=function(b,c){m(b,c,void 0===b||"string"==typeof b||b instanceof a,"String or Cldr instance")},o=function(a){return null!==a&&""+a=="[object Object]"},p=function(a,b){m(a,b,void 0===a||o(a),"Plain Object")},q=function(b){return b instanceof a?b:new a(b)};return c.load=function(b){k(b,"json"),p(b,"json"),a.load(b)},c.locale=function(a){return n(a,"locale"),arguments.length&&(this.cldr=q(a),b(this.cldr)),this.cldr},c._alwaysArray=h,c._createError=f,c._formatMessage=e,c._isPlainObject=o,c._validate=g,c._validateCldr=i,c._validateDefaultLocale=j,c._validateParameterPresence=k,c._validateParameterRange=l,c._validateParameterTypePlainObject=p,c._validateParameterType=m,c});