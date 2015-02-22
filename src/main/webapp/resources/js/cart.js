$(document).ready(function() {
    
	// List of navigation anchor tags 
    var navigationSteps = $('ul.navsetup li a');
    
    // Content of individual navigation sections
    var sections = $('.navsection');

    // Hide all sections by default
    sections.hide();

    //onClick navigation anchor tags
    navigationSteps.click(function(e)
    {
    	// Prevent default action event 
        e.preventDefault();
        
        var $target = $($(this).attr('href'));
        var $item = $(this).closest('li');
        
        // Switch for navigation anchor tags 
        if (!$item.hasClass('disabled')) {
            navigationSteps.closest('li').removeClass('active'); // Disable anchor with active class
            $item.addClass('active'); // Activate selected navigation anchor
            sections.hide(); // Hide Content
            $target.show(); // Show selected content div
        }
    });
    
    $('ul.navsetup li.active a').trigger('click');
    
    
    $('#activate-finalization').on('click', function(e) {
        $('ul.navsetup li:eq(1)').removeClass('disabled');
        $('ul.navsetup li a[href="#finalization"]').trigger('click');
        // Remove button
        $(this).remove();
    });
    
    $('#activate-invoice').on('click', function(e) {
        $('ul.navsetup li:eq(2)').removeClass('disabled');
        $('ul.navsetup li a[href="#invoice"]').trigger('click');
        // Remove button
        $(this).remove();
    });  
    
    
});