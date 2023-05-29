# save.image("Rscripts/cdp_regression_coda_response.RData")


H = coda.base::coordinates(Y, basis = coda.base::sbp_basis(BasisY))
colnames(H) = paste0('ilr.', 1:ncol(H))

str_y = colnames(H)
if(ncol(H) > 1) str_y = sprintf("cbind(%s)", paste(colnames(H), collapse=','))
str_x = paste(colnames(X), collapse='+')
str_frm = sprintf("%s ~ %s", str_y, str_x)

. = as.data.frame(cbind(H,X))
LM = eval(parse(text = sprintf("lm(%s, .)", str_frm)))

R2 = sum(scale(LM$fitted.values, scale = FALSE)^2) / sum(scale(H, scale = FALSE)^2) 

B = coef(LM)
S = sqrt(diag(vcov(LM)))
P = 2*pt(-abs(B/S), LM$df.residual)
O = sprintf("% 0.6f (%0.6f) p=%s", B, S, ifelse(P<0.0001, "<0.0001", sprintf("%0.5f", P)))
dim(O) = dim(B)
colnames(O) = colnames(B)
rownames(O) = rownames(B)

RES = unlist(anova(LM, update(LM, .~1), test = 'Wilks')[2,c('approx F', 'num Df' , 'den Df' ,'Pr(>F)')])
output = gsub("\"", "", capture.output(O))
output = c("", "Coefficients:", output)
output = c(deparse(LM$call), output)
output = c("Call:", output)
output = c(output, "---", "")
output = c(output, sprintf("Multiple R-squared:  %0.3f", R2))
output = c(output, sprintf("F-statistic (Wilks' approx.): %0.3f on %.1f and %.1f DF,  p-value: %0.5f", RES[1], RES[2], RES[3], RES[4]))
cat(output, sep ='\n')

new_data = list()
if (V1) new_data = c(new_data, setNames(apply(LM$residuals, 2, identity, simplify = FALSE), paste0('r.', colnames(H))))
if (V2) new_data = c(new_data, setNames(apply(LM$fitted.values, 2, identity, simplify = FALSE), paste0('f.', colnames(H))))





# # Create graphs
# graphnames <- list()
# for (n in 1:nparts)
# {
#   name <- generateFileName(paste(tempdir(),paste("Plots_of_residuals_",names(H[n]),sep=""),sep="\\"))
#   svg(name)
#   LM.temp <- lm(as.matrix(H[n])~as.matrix(X))
#   oldpar <- par(oma=c(0,0,3,0), mfrow=c(2,2))
#   title <- paste(names(H[n])," ~ ",names(X))
#   plot(LM.temp,sub.caption=title)  # Plot the model information
#   par(oldpar)
#   dev.off()
#   graphnames[n] <- name
# }



#PART NOVA. ARA TE EN COMPTE ELS NOMS DE TOTES LES X I TRANSPOSA COEFICIENTS PER PODER-LOS INVERTIR
NDF<-as.data.frame(as.factor(c("intercept",names(X))))
names(NDF)<-paste("Coefficients")
NDF=cbind.data.frame(NDF,LM$coefficients)
#FI PART NOVA.


# Ooutput
cdp_res = list(
  'text' = output,
  'dataframe' = list(), #list('coefficients' = NDF),
  'graph' = list(), #graphnames,
  'new_data' = new_data
)

save.image("imgf.RData")