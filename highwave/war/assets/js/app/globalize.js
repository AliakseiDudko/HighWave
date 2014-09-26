var culture = window.navigator.language || "ru-RU";
var language = culture.substring(0, 2);

define([ "globalize/globalize", "json!globalize/cldr/supplemental/likelySubtags.json", "json!globalize/cldr/supplemental/timeData.json",
        "json!globalize/cldr/supplemental/weekData.json", "json!globalize/cldr/main/" + language + "/ca-gregorian.json",
        "json!globalize/cldr/main/" + language + "/numbers.json", "json!globalize/cldr/main/" + culture + "/ca-gregorian.json",
        "json!globalize/cldr/main/" + culture + "/numbers.json", "globalize/globalize/number", "globalize/globalize/date" ], function(
        Globalize, likelySubtags, timeData, weekData, langCaGregorian, langNubers, cultureCaGregorian, cultureNubers) {
    Globalize.load(likelySubtags);
    Globalize.load(timeData);
    Globalize.load(weekData);
    Globalize.load(langCaGregorian);
    Globalize.load(langNubers);
    Globalize.load(cultureCaGregorian);
    Globalize.load(cultureNubers);

    var globalize = Globalize(culture);

    globalize.formatInt = function(value) {
        return value ? this.formatNumber(value) : "";
    };

    globalize.formatFloat = function(value, fractionDigits) {
        return this.formatNumber(value, {
            minimumFractionDigits: fractionDigits,
            maximumFractionDigits: fractionDigits
        });
    };

    globalize.formatIsoDate = function(value) {
        var date = new Date(value);
        return this.formatDate(date, {
            pattern: "dd/MM/YY"
        });
    };

    return globalize;
});