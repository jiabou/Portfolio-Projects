# INSY7314_PPOE_Part_2

YouTube video:
https://youtu.be/dbn2Z0WXaFM

GitHub repo:
https://github.com/Silver-Pond/INSY7314_PPOE_Part_2.git

## To remove warnings in the browser due to self-signed certificate:

Making the Certificate Trusted (Windows)
To trust the cert and remove warnings when the app is run:

Press Win + R, type certmgr.msc, and press Enter.
Expand Trusted Root Certification Authorities and select Certificates.
Right-click in the right pane, select All Tasks > Import...
Import ssl/cert.pem as a certificate file.
Make sure the certificate store is Trusted Root Certification Authorities.
Finish the wizard, accept warnings, and restart your browser.
