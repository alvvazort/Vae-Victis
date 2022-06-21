(($) => {
    $(document).ready(() => {
        $('.open-cards').click(() => {
            $('.cardsmenu').addClass('opened');
            $('.open-cards').addClass('hidden');
        });

        $('.close-cards').click(() => {
            $('.cardsmenu').removeClass('opened');
            $('.open-cards').removeClass('hidden');
        });

    });
    
})(jQuery);