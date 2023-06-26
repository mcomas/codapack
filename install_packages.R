available_packages = rownames(installed.packages())
R_repo = 'http://cran.us.r-project.org'

if(!"rJava" %in% available_packages) install.packages('rJava', repos = R_repo)
if(!"coda.base" %in% available_packages) install.packages('coda.base', repos = R_repo)
if(!"zCompositions" %in% available_packages) install.packages('zCompositions', repos = R_repo)
if(!"fpc" %in% available_packages) install.packages('fpc', repos = R_repo)