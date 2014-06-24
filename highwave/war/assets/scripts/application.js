MyApp = new Backbone.Marionette.Application();

MyApp.addRegions({
	generatorRegion : "#generator"
});

Bank = Backbone.Model.extend({});

Banks = Backbone.Collection.extend({
	model : Bank
});

BankView = Backbone.Marionette.ItemView.extend({
	template : "#bank-template",
	tagName : "tr",
	className : "bank"
});

BanksView = Backbone.Marionette.CompositeView.extend({
	tagName : "table",
	id : "banks",
	template : "#banks-template",
	itemView : BankView,

	appendHtml : function(collectionView, itemView) {
		collectionView.$("tbody").append(itemView.el);
	}
});

MyApp.addInitializer(function(options) {
	var banksView = new BanksView({
		collection : options.banks
	});
	MyApp.generatorRegion.show(banksView);
});

$(document).ready(function() {
	var banks = new Banks([ new Bank({
		name : "BelarusBank"
	}), new Bank({
		name : "Empty"
	}) ]);

	MyApp.start({
		banks : banks
	});

	banks.add();
	banks.add();
});