MyApp.ProfitApp.DepositList = function() {
	var DepositList = {};

	var DepositDetailView = Backbone.Marionette.ItemView.extend({
		template : "#deposit-detail-template"
	});

	var DepositView = Backbone.Marionette.ItemView.extend({
		template : "#deposit-template",

		events : {
			"click" : "showDepositDetail"
		},

		showDepositDetail : function() {
			var detailView = new DepositDetailView({
				model : this.model
			});
			MyApp.modal.show(detailView);
		}
	});

	var DepositListView = Backbone.Marionette.CompositeView.extend({
		tagName : "table",
		id : "depositList",
		template : "#deposit-list-template",
		childView : DepositView,

		showMessage : function(message) {
			console.log(message);
		},

		attachHtml : function(collectionView, itemView) {
			collectionView.$("tbody").append(itemView.el);
		},

		initialize : function() {
			_.bindAll(this, "showMessage");
			var self = this;
			MyApp.vent.on("search:error", function() {
				self.showMessage("Error, please retry later :s");
			});
			MyApp.vent.on("search:noSearchTerm", function() {
				self.showMessage("Hummmm, can do better :)");
			});
			MyApp.vent.on("search:noResults", function() {
				self.showMessage("No books found");
			});
		}
	});

	var SearchView = Backbone.View.extend({
		el : "#search",

		initialize : function() {
			var self = this;
			MyApp.vent.on("search:start", function() {
			});
			MyApp.vent.on("search:stop", function() {
			});
			MyApp.vent.on("search:term", function(term) {
				self.$("#searchTerm").val(term);
			});
		},

		events : {
			"change #searchTerm" : "search"
		},

		search : function() {
			var searchTerm = this.$("#searchTerm").val().trim();
			if (searchTerm.length > 0) {
				MyApp.vent.trigger("search:term", searchTerm);
			} else {
				MyApp.vent.trigger("search:noSearchTerm", searchTerm);
			}
		}
	});

	DepositList.showDeposits = function(deposits) {
		var depositListView = new DepositListView({
			collection : deposits
		});
		MyApp.ProfitApp.layout.results.show(depositListView);
	};

	MyApp.vent.on("layout:rendered", function() {
		var searchView = new SearchView();
		MyApp.ProfitApp.layout.search.attachView(searchView);
	});

	return DepositList;
}();

MyApp.vent.on("layout:rendered", function() {
	MyApp.ProfitApp.DepositList.showDeposits(MyApp.ProfitApp.Deposits);
});
