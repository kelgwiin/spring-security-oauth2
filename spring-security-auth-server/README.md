Asymmetric Keypair
==================

keytool -genkeypair -alias mytest 
                    -keyalg RSA 
                    -keypass mypass 
                    -keystore mytest.jks 
                    -storepass mypass


keytool -genkeypair -alias demo-jks
                    -keyalg RSA 
                    -keypass strong-pass 
                    -keystore keystore.jks 
                    -storepass strong-store-pass