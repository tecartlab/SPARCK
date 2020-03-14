<?php
    #Using: http://stackoverflow.com/questions/409999/getting-the-location-from-an-ip-address#16589641
    $ip = $_SERVER['REMOTE_ADDR'];
    date_default_timezone_set('CET');
    $machine = $_POST["machineID"]; //You have to get the form data
    $license = $_POST["license"];
    $serial = $_POST["serial"];
    $build = $_POST["build"];
    $version = $_POST["version"];
    $release = $_POST["release"];
    $file = fopen('SparckStartupLogs.txt', 'a+'); //Open your .txt file
    # ALTERNATE Date Display: date('l jS \of F Y H:i:s') http://php.net/manual/en/function.date.php
    $content = PHP_EOL . date('Y/m/d H:i:s'). "\t" .$ip. "\t" .$release. "\t" .$version. "\t" .$build. "\t" .$license. "\t"  .$serial. "\t" . $machine;
    fwrite($file , $content); //Now lets write it in there
    # AND NOW TRY TO GET THE GEOLOCATION:
    $details = json_decode(file_get_contents("http://ipinfo.io/{$ip}/json"));
    $content = "\t" .$details->country. "\t" .$details->city;
    fwrite($file , $content); //Now lets write it in there
    #AND NOD CLOSE THE FILE
    fclose($file ); //Finally close our .txt
?>