$(document).ready(function() {

	var searchFormType = $("[id='searchForm:type']");
	var searchFormType2 = $("[id='searchForm2:type']");
	var searchFormTypeAuthor = $("[id='searchForm-author:type']");
	var searchFormTypeClient = $("[id='searchForm-client:type']");
	var searchFormTypePublisher = $("[id='searchForm-publisher:type']");
	
	var lastGenreBooks = $("#carousel-genre");
	var recentBooks = $("#carousel-recent");
	var booksOnSale = $("#carousel-sale");
	var rssFeeds = $("#carousel-rss");
	
	recentBooks.owlCarousel(
	{
		autoPlay : true,
		stopOnHover : true,
		afterAction : afterActionRecent,
		pagination : false
	});

	booksOnSale.owlCarousel(
	{
		autoPlay : true,
		stopOnHover : true,
		afterAction : afterActionSale,
		pagination : false
	});
	
	lastGenreBooks.owlCarousel(
	{
		autoPlay : true,
		stopOnHover : true,
		afterAction : afterActionGenre,
		pagination : false
	});
	
	rssFeeds.owlCarousel(
	{
		autoPlay : 10000,
	    slideSpeed : 500,
	    singleItem: true,
	    pagination : false
	});

	var prevRecentItem;
	function afterActionRecent()
	{
		if(prevRecentItem != null) prevRecentItem.css("border-left", ""); 
		var item = this.owl.currentItem;
		prevRecentItem = $("#carousel-recent").find(".bookWrapper").eq(item).css("border-left","0"); 
	}


	var prevSaleItem;
	function afterActionSale()
	{
		if(prevSaleItem != null) prevSaleItem.css("border-left", ""); 
		var item = this.owl.currentItem;
		prevSaleItem = $("#carousel-sale").find(".bookWrapper").eq(item).css("border-left","0"); 
	}
	
	var prevGenreItem;
	function afterActionGenre()
	{
		if(prevGenreItem != null) prevGenreItem.css("border-left", ""); 
		var item = this.owl.currentItem;
		prevGenreItem = $("#carousel-genre").find(".bookWrapper").eq(item).css("border-left","0"); 
	}
	
	searchFormType.attr('value', 'Title');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormType.attr('value', $(this).text());
	});
	
	searchFormType2.attr('value', 'Title');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormType2.attr('value', $(this).text());
	});
	
	searchFormTypeAuthor.attr('value', 'Author');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormTypeAuthor.attr('value', $(this).text());
	});
	
	searchFormTypeClient.attr('value', 'Id');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormTypeClient.attr('value', $(this).text());
	});
	
	searchFormTypePublisher.attr('value', 'Publisher');
	$('.search-type .dropdown-menu li a').click(function(){
		$('.search-dropdown').html($(this).text() + ' <span class="caret"></span>'); 
		searchFormTypePublisher.attr('value', $(this).text());
	});
	
	$('#signInModal').on('shown.bs.modal', function (e) {
		$("[id='signInForm:username']").focus(); 
	});
	
	$('#addToCart').click(function() {
		this.blur();
		$('.alert-message').fadeTo(0, 500).slideDown(500, function() {
			  $(this).show(); 
	    });
		window.setTimeout(function() {
		    $(".alert-message").fadeTo(500, 0).slideUp(500, function(){
		        $(this).hide(); 
		    }); }, 3000);
	
	});
	
});