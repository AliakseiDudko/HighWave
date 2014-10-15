define([ "marionette", "text!templates/account-statement-list-item-template.html", "text!templates/account-statement-list-template.html" ],
        function(Marionette, templateItemHtml, templateHtml) {
            var AccountStatementItemView = Marionette.ItemView.extend({
                template: _.template(templateItemHtml),
                className: function() {
                    return this.model.get("isLast") ? "danger" : "";
                },
                tagName: "tr"
            });

            var AccountStatementView = Marionette.CompositeView.extend({
                template: _.template(templateHtml),
                className: "table table-striped",
                tagName: "table",
                childView: AccountStatementItemView
            });

            return AccountStatementView;
        });