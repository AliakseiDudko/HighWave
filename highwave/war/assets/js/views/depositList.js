define([ "marionette", "text!templates/deposit-list-item-template.html", "text!templates/deposit-list-emtpy-template.html",
        "text!templates/deposit-list-template.html" ], function(Marionette, templateItemHtml, emptyTemplateHtml, templateHtml) {
    var DepositView = Marionette.ItemView.extend({
        tagName: "tr",
        template: _.template(templateItemHtml),

        events: {
            "click": function() {
                Backbone.Events.trigger("app:showDepositDetails", this.model);
            }
        }
    });

    var DepositListEmptyView = Marionette.ItemView.extend({
        tagName: "tr",
        template: _.template(emptyTemplateHtml)
    });

    var DepositListView = Marionette.CompositeView.extend({
        template: _.template(templateHtml),
        className: "table table-striped table-hover",
        tagName: "table",
        childView: DepositView,
        emptyView: DepositListEmptyView
    });

    return DepositListView;
});