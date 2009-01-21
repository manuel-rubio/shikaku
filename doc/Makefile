all: memoria.pdf

memoria.pdf: memoria.dvi memoria.tex
	dvipdf memoria.dvi
	rm -f memoria.dvi

memoria.dvi: memoria.tex
	latex memoria.tex
	latex memoria.tex
	rm -f memoria.log
	rm -f memoria.aux
	rm -f memoria.toc

clean:
	rm -f memoria.pdf
	rm -f memoria.dvi
	rm -f memoria.log
	rm -f memoria.toc
	rm -f memoria.aux
