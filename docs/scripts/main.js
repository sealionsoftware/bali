var COMPILE_SERVER = document.currentScript.getAttribute('compileServer');

console.log('Setting up bali tutorial');

$(document).ready(function() {

    function setupConsole(target, path, processReply){

        var results = $('<div class="results"></div>');

        var play = $('<div class="play"></div>')
            .click(function(){
                console.log('Submitting code');

                $.ajax({
                    url: path,
                    method: 'POST',
                    data: target.text(),
                    contentType: 'text/plain',
                    success: function(reply) {
                        target.addClass('compiledOK');
                        results.empty();
                        if (reply == null || reply.length == 0) {
                            results.hide();
                        } else {
                            processReply(results, reply);
                            results.fadeIn();
                        }
                    },
                    error: function(reply) {
                        target.addClass('compilationFailed');
                        results.empty();
                        results.html(reply.responseJSON.message);
                        results.fadeIn();
                    }
                });

            });

        $(target)
            .attr('contentEditable', 'true')
            .addClass('interpreted')
            .bind('input', function(){
                target.removeClass('compiledOK compilationFailed');
            })
            .wrap($('<div class="baliClient"></div>'))
            .wrap($('<div class="playAnchor"></div>'))
            .parent()
            .append(play)
            .after(results);
    }

    $('.executable').each(function(){setupConsole($(this), '//' + COMPILE_SERVER + '/fragment', function(results, reply){
        if ($.isArray(reply)){ $.each(reply, function(key, value){
            results.append(print(value) + '<br/>');
        })} else results.append(print(reply));
    })});

    $('.evaluable').each(function(){setupConsole($(this), '//' + COMPILE_SERVER +'/expression', function(results, reply){
        results.append(print(reply));
    })});

    $('div[contenteditable]').keydown(function(e) {
        // trap the return key being pressed
        if (e.keyCode === 13) {
            // insert 2 br tags (if only one br tag is inserted the cursor won't go to the next line)
            document.execCommand('insertHTML', false, '\n');
            // prevent the default behaviour of return key pressed
            return false;
        }
    });

});

function print(value){
    return value == null ? '' : '' + value;
}
