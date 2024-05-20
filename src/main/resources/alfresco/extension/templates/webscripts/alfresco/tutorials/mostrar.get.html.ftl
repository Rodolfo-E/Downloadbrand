<!DOCTYPE html>
<html>

<head lang="es">
    <meta charset="UTF-8">
    <title>Formulario de Entrada</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
     

        .container {
            width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 5px;
            
        }

        h2 {
            font-size: 24px;
            margin-bottom: 20px;
            color: #333;
            text-align: center;
        }

        label {
            display: block;
            font-size: 16px;
            margin-bottom: 5px;
        }

        input[type="text"],
        input[type="checkbox"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 16px;
        }

        input[type="checkbox"] {
            width: auto;
            margin-top: 5px;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 12px 20px;
            font-size: 18px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

          input[type="reset"] {
            background-color: #06b300;
            color: #fff;
            border: none;
            padding: 12px 20px;
            font-size: 18px;
            cursor: pointer;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }
        input[type="reset"]:hover {
            background-color: #078f02fd;
        }
        .center{
            text-align: center;
        }
        /* Estilos adicionales para mejorar la experiencia de usuario */
    </style>
</head>

<body>
    <div class="container">
        <h2>Marca de agua en el Documento</h2>
        <form action="download" method="get">
            <input type="hidden" id="campoNodeRef" value="${(node)!''}" name="nodeRef">
            <label for="x">Coodernada x de la imagen:</label>
            <input type="text" id="x" name="x" ><br><br>

            <label for="y">Coodernada y de la imagen::</label>
            <input type="text" id="y" name="y" ><br><br>

            <label for="width">Ancho de la imagen:</label>
            <input type="text" id="width" name="width" ><br><br>

            <label for="height">Altura de la imagen:</label>
            <input type="text" id="height" name="height" ><br><br>
            
            <label for="height">Transparencia de la img(0.0 a 1.0)</label>
            <input type="text" id="height" name="trans" ><br><br>

            <label for="chkpages">Habilitar firma para todas las páginas:</label>
            <input type="checkbox" id="chkpages" name="chkpages" value="true"><br><br>

          
            <div class="center">
            <input type="submit" value="Descargar">
            <input type="reset" value="Limpiar">
        </div>
        </form>
    </div>
</body>

</html>