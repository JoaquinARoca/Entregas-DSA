$(document).ready(function() {
    $('#search-btn').click(function() {
      var username = $('#github-username').val();
      
      if (username) {
        // Hacemos la solicitud AJAX a la API de GitHub
        $.ajax({
          url: `https://api.github.com/users/${username}/repos`,
          method: 'GET',
          success: function(repos) {
            // Limpiamos la tabla antes de agregar los datos nuevos
            $('#repos-table tbody').empty();
            
            // Iteramos sobre los repositorios
            repos.forEach(function(repo) {
              var row = `<tr>
                           <td><a href="${repo.html_url}" target="_blank">${repo.name}</a></td>
                           <td>${repo.description || 'No description'}</td>
                           <td>${repo.stargazers_count}</td>
                         </tr>`;
              $('#repos-table tbody').append(row);
            });
          },
          error: function() {
            alert('User not found or API error.');
          }
        });
      } else {
        alert('Please enter a GitHub username');
      }
    });
  });
  