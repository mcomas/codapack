print("Hello World from JavaScriptFile.js");



var fun1 = function(name) {
    print('Hi there from Javascript, ' + name);
    return "greetings from javascript";
};

var returnFecha = function(nada){
    width = nada;
    var fecha = new Date(); //Obtiene un objeto fecha actual.
    var dia;
    var vardia = fecha.getDay();    //getDay() devuelve el dia de la semana.(0-6).
    switch (vardia) { //Algoritmo para calcular el dia según el número.
        case 0:
            dia = "Diumenge";
            break;
        case 1:
            dia = "Dilluns";
            break;
        case 2:
            dia = "Dimarts";
            break;
        case 3:
            dia = "Dimecres";
            break;
        case 4:
            dia = "Dijous";
            break;
        case 5:
            dia = "Divendres";
            break;
        case 6:
            dia = "Dissabte";
            break;
    }

    var mes;
    //Algoritmo para calcular el mes según el número.
    switch (fecha.getMonth() + 1) { //getMonth() devuelve el mes (0-11).
        case 1:
            mes = "Gener";
            break;
        case 2:
            mes = "Febrer";
            break;
        case 3:
            mes = "Març";
            break;
        case 4:
            mes = "Abril";
            break;
        case 5:
            mes = "Maig";
            break;
        case 6:
            mes = "Juny";
            break;
        case 7:
            mes = "Juliol";
            break;
        case 8:
            mes = "Agost";
            break;
        case 9:
            mes = "Septembre";
            break;
        case 10:
            mes = "Octubre";
            break;
        case 11:
            mes = "Novembre";
            break;
        case 12:
            mes = "Decembre";
            break;
    }

    var diames = (fecha.getDate());   //getDate() devuelve el dia(1-31).
    var ano = fecha.getFullYear();  //getFullYear() devuelve el año(4 dígitos).
    var hora = fecha.getHours();    //getHours() devuelve la hora(0-23).
    var min = fecha.getMinutes();   //getMinutes() devuelve los minutos(0-59).
    if ((min >= 0) && (min < 10)) {    //Algoritmo para añadir un cero cuando el min tiene 1 cifra.
        min = "0" + min;
    }
    var salida = "" + dia + ", " + diames + " de ";
    var salida = salida + mes + " de " + ano + " , ";
    var salida = salida + hora + ":" + min + " h \n <br>";
    //document.write(salida);
    return salida;
};

function move2(_width) {
    console.log("document");
    console.log(document.URL);
    var elem = document.getElementById("myBar");
    var width = _width;
    var id = setInterval(frame, 10);
    function frame() {
        if (width >= 100) {
            clearInterval(id);
        } else {
            width++;
            elem.style.width = width + '%';
            document.getElementById("label").innerHTML = width * 1 + '%';
        }
    }
}

function myFunction() {
    var elem = document.getElementById("myBar");
    width = 0;
    elem.style.width = width + '%';
    document.getElementById("label").innerHTML = width * 1 + '%';
  }

  function createRow(){
    print("Hello World from function createRow from JavaScriptFile.js");
  }

 function createGraf(){
    try
    {
        var ctx = document.getElementById('myChart').getContext('2d');
        var chart = new Chart(ctx, {
            type: 'doughnut',
            data:{
            datasets: [{
                data: [60,18,10, 8, 4],
                backgroundColor: ['#42a5f5', 'red', 'green','blue','violet'],
                label: 'Comparacion de navegadores'}],
                labels: ['Google Chrome','Safari','Edge','Firefox','Opera']},
            options: {responsive: false}
        });
    }catch(error){
        console.error(error);
    }
    
    
 }

 function createGraf2(lastVal){
    var ctx1 = document.getElementById('myChart2').getContext('2d');
    var chart = new Chart(ctx1, {
        type: 'line',
        data: {
            labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
            datasets: [{
                label: 'Ganancias',
                backgroundColor: '#42a5f5',
                borderColor: 'gray',
                data: [7, 8, 5, 2, 8, 10, 7,-7,4,9,-8,lastVal]
            }		
            ]},
        options: {responsive: false}
    });
 }

 function createGraf3(v1,v2,v3,v4,v5){
    var ctx2 = document.getElementById('myChart3').getContext('2d');
    var chart = new Chart(ctx2, {
        type: 'radar',
        data: 	
        {
            datasets: [{
                data: [document.getElementById('v1').value,v2,v3, v4, v5],
                backgroundColor: ['#42a5f5', 'red', 'green','blue','violet'],
                label: 'Comparacion de navegadores'
            }],
            labels: [
                'Google Chrome',
                'Safari',
                'Edge',
                'Firefox',
                'Opera'
            ]},
        options: {responsive: false}
    });
 }


