available_packages = rownames(installed.packages())
R_repo = 'http://cran.us.r-project.org'

if(!exists('TYPE')) TYPE = 'source'
if(!exists('LIB')) LIB = 'Rlibraries'
install_packages = function(pname){
    install.packages(pname, repos = R_repo, type = TYPE, dependencies = NA, lib = LIB)
}
install_packages('rJava')
install_packages('coda.base')
install_packages('zCompositions')
install_packages('fpc')
install_packages('MASS')
install_packages('knitr')