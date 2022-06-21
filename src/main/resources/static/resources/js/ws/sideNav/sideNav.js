(($) => {
    $(document).ready(() => {
        $('#desplegable').click(() => {
            $('.sideMenu').addClass('opened');
            $('.botonSocial').addClass('hidden');
        });

        $('#close-btn').click(() => {
            $('.sideMenu').removeClass('opened');
            $('#desplegable').removeClass('hidden');
        });

        $('#inviteFriendToLobby button').click(function() {
            $.ajax({
                url: $(this).parent().attr('data-action'),
                method: 'post',
                data: {
                    username: $(this).parent().attr('data-username')
                },
                success: res => {
                    //console.log(res);
                    window.location.reload();
                }
            });
        });

    });
})(jQuery);

