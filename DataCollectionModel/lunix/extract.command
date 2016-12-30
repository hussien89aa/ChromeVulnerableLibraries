
 while read version_chrome
do
 #version_chrome="54.0.2840.79"
 echo "checkout version: ${version_chrome}";
cd /Path_in_PC/chromium/src;
#git reset --hard;

#git stash;
sudo git checkout "tags/${version_chrome}" -b "s${version_chrome}" ;
#gclient sync --jobs 16;

 cd ../;
cd ../;
 echo "start dependency-check for version: ${version_chrome}";
dependency-check --project Testing --out . --scan  chromium/src --format XML  ;
mv dependency-check-report.xml "${version_chrome}.xml";
#echo "$NAME"
#sudo rm -rf third_party;
done < StableVersions.txt
 
