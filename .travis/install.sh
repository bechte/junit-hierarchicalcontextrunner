#!/usr/bin/env sh

# Decrypt secure files
openssl aes-256-cbc -K $encrypted_e843a70077f8_key -iv $encrypted_e843a70077f8_iv -in $ENCRYPTED_GPG_KEY_LOCATION -out $GPG_KEY_LOCATION -d

# Import GPG Secret Key to key store
gpg --import $GPG_KEY_LOCATION
