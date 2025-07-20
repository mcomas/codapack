cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X)
  if(!is.null(cond1)) return(cond1)
  
}
cdp_analysis = function(){
  save.image("Rscripts/cdp_principal_balances.RData")
  method = tolower(V1)
  if(method == "exact" & ncol(X) > 15) {
    stop("CoDaPack supports up to 15 parts. To compute principal balances \nwith more than 15 parts, please use the R package coda.base.")
  } 
  B = coda.base::pb_basis(X, method)
  P = sign(t(B))
  rownames(P) = NULL
  output = "<b>Sequential binary partition:</b>"
  matriu_a_html <- function(m) {
    html <- "<table>"
    
    # Capçalera
    html <- paste0(html, "  <tr>")
    for (colname in colnames(m)) {
      html <- paste0(html, "<th>", colname, "</th>")
    }
    html <- paste0(html, "</tr>")
    
    # Files
    for (i in seq_len(nrow(m))) {
      html <- paste0(html, "  <tr>")
      for (j in seq_len(ncol(m))) {
        html <- paste0(html, "<td  align='right'>", m[i, j], "</td>")
      }
      html <- paste0(html, "</tr>")
    }    
    html <- paste0(html, "</table>")
    return(html)
  }
  output = c(output, capture.output(cat(matriu_a_html(P))))
  balances = list()
  if(V2){
    balances = as.data.frame(coda.base::coordinates(X, B))
    colnames(balances) = paste0('pb.', 1:ncol(balances))
  }
  list(
    'text' = output,
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = balances
  )
}
  
  