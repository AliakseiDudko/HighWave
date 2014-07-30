Backbone.Marionette.TemplateCache.prototype.loadTemplate = function(templateId) {
    var template;
    var templateUrl = "/assets/template/" + templateId + ".html";

    try {
        template = Marionette.$(templateId).html();
    } catch (e) {
    }
    
    if (!template || template.length === 0) {
        $.ajax({
            url: templateUrl,
            success: function(data) {
                template = data;
            },
            async: false
        });
        if (!template || template.length === 0) {
            throw "NoTemplateError - Could not find template: '" + templateUrl + "'";
        }
    }
    return template;
};