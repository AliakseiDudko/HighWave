MyApp.ProfitApp = function() {
	var ProfitApp = {};

	var Layout = Backbone.Marionette.LayoutView.extend({
	    template : "#profit-layout",

	    regions : {
	        search : "#search",
	        results : "#results"
	    }
	});

	var Deposit = Backbone.Model.extend();

	var Deposits = Backbone.Collection.extend({
	    model : Deposit,

	    initialize : function() {
		    var self = this;

		    _.bindAll(this, "search");
		    MyApp.vent.on("search:term", function(term) {
			    self.search(term);
		    })

		    this.maxResults = 40;
		    this.page = 0;
		    this.loading = false;
		    this.totalItems = null;
	    },

	    search : function(searchTerm) {
		    this.page = 0;

		    var self = this;
		    this.fetchDeposits(searchTerm, function(deposits) {
			    if (deposits.length > 0) {
				    self.reset(deposits);
			    } else {
				    MyApp.vent.trigger("search:noResults");
			    }
		    });
	    },

	    fetchDeposits : function(searchTerm, callback) {
		    if (this.loading) {
			    return true;
		    }

		    this.loading = true;

		    var self = this;
		    MyApp.vent.trigger("search:start");

		    var query = "";
		    $.ajax({
		        url : "/_ah/api/deposits/v0/deposits",
		        dataType : "json",
		        data : query,
		        success : function(res) {
			        MyApp.vent.trigger("search:stop");
			        if (res.items.length == 0) {
				        callback([]);
				        return [];
			        }
			        if (res.items) {
				        self.page++;
				        self.totalItems = res.totalItems;
				        var searchResults = [];
				        _.each(res.items, function(item) {
					        searchResults[searchResults.length] = new Deposit({
					        	depositId: item.id,
					            name : item.displayName,
					            currency : item.currency
					        });
				        });
				        callback(searchResults);
				        self.loading = false;
				        return searchResults;
			        } else if (res.error) {
				        MyApp.vent.trigger("search:error");
				        self.loading = false;
			        }
		        }
		    });
	    }
	});

	ProfitApp.Deposits = new Deposits();

	ProfitApp.initializeLayout = function() {
		ProfitApp.layout = new Layout();

		ProfitApp.layout.on("show", function() {
			MyApp.vent.trigger("layout:rendered")
		});

		MyApp.content.show(MyApp.ProfitApp.layout);

	};

	return ProfitApp;
}();

MyApp.addInitializer(function() {
	MyApp.ProfitApp.initializeLayout();
	MyApp.vent.trigger("search:term", "Day 7");
});