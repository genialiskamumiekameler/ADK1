# Måste köras innan man kör sort för att få rätt teckenkodning.
#export LC_COLLATE=C 

# Kör tokenizer på korpus filen, sorterar den sedan och lägger det på tmp minnet
#/info/adk16/labb1/tokenizer < /info/adk16/labb1/korpus | sort > /var/tmp/tokenizerFile 

KORPUS = /info/adk13/labb1/korpus
TOKENFILE = /info/adk13/labb1/tokenizer
INDEXFILE = /var/tmp/tokenizerFile
HASHFILE = /var/tmp/hashFile

Concordance : 
	javac Concordance.java

$(INDEXFILE) : $(KORPUS)
	($TOKENFILE) < $(KORPUS) | sort > ($INDEXFILE)