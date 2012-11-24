Our INF2100 Project
===================

Cflat, the syntax is like a warm summer breeze
----------------------------------------------

![Success!](http://freelancefolder.com/wp-content/uploads/truth-about-success.jpg "Success!")

Description
-----------

Java project for INF2100, for implementing a compiler in Java.

Vim tips&tricks
---------------

    map <F8> :!./compile_and_test.sh<CR>

    Hvilken av disse er best?

    http://www.vim.org/scripts/script.php?script_id=1954
    http://www.vim.org/scripts/script.php?script_id=1154

Relevant URLs
-------------

More sample programs:
http://heim.ifi.uio.no/inf2100/oblig/test/

Corrections to the precode:
http://www.uio.no/studier/emner/matnat/ifi/INF2100/h12/kursprat.html#/discussion/133/inf2100-host-2012-feil-i-prekodefiler


Files that can be changed
-------------------------

Part 0

* CharRenerator.java
* Scanner.java
* Token.java
* Cflat.java

Links to Jonas
--------------

    http://futz.me/frugi http://some/url/to/send

En slags minimap (aktiveres med ctrl-space)
-------------------------------------------

" --- Minimap toggle just by changing the font ---

```
function! ToggleMinimapOn()
  " make font small
  set columns=100
  set lines=95
  set guifont=ProggyTinyTT\ 11
  color tolerable
  nmap <c-space> :ToggleMinimapTiny<CR>
endfunction
function! ToggleMinimapTiny()
  " make font small
  set columns=100
  set lines=120
  set guifont=ProggyTinyTT\ 5
  color oceandeep
  nmap <c-space> :ToggleMinimapOff<CR>
endfunction
function! ToggleMinimapOff()
  " make font normal
  set columns=182
  set lines=55
  set guifont=Monospace\ 11
  color wintersday
  nmap <c-space> :ToggleMinimapOn<CR>
endfunction
command! ToggleMinimapOn call ToggleMinimapOn()
command! ToggleMinimapTiny call ToggleMinimapTiny()
command! ToggleMinimapOff call ToggleMinimapOff()
nmap <c-space> :ToggleMinimapOn<CR>
``

