print("Hello World from JavaScriptFile.js");

var fun1 = function(name) {
    print('Hi there from Javascript, ' + name);
    return "greetings from javascript";
};

var returnFecha = function(nada){
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


function move(width) {
    console.log("prova");
    //var elem = document.getElementsByName("myBar");

    console.log("Element :",elem);
    //var elementNameExists = !!document.getElementsByName("myBar");
    console.log("Is Not null ? ",elementNameExists);

    //var width = 1;
    var id = setInterval(frame, 1);
    function frame() {
        if (width >= 100) {
            clearInterval(id);
        } else {
            width++;
            //elem.style.width = width + '%';
        }
    }
    return width;
}