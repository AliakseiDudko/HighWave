define([ "marionette", "text!templates/account-statement-list-item-template.html", "text!templates/account-statement-list-template.html" ],
        function(Marionette, templateItemHtml, templateHtml) {
            var AccountStatementItemView = Marionette.ItemView.extend({
                tagName: "tr",
                template: _.template(templateItemHtml)
            });

            var AccountStatementView = Marionette.CompositeView.extend({
                template: _.template(templateHtml),
                className: "table table-striped",
                tagName: "table",
                childView: AccountStatementItemView
            });

            return AccountStatementView;
        });