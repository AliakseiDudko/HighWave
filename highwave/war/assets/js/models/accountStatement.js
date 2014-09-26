define([ "backbone" ], function(Backbone) {
    var AccountStatementItem = Backbone.Model.extend();

    var AccountStatement = Backbone.Collection.extend({
        model: AccountStatementItem
    });

    return AccountStatement;
});