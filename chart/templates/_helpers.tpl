{{- define "hname" -}}
{{- printf "%s-%s" .Values.general.component .Values.general.serviceImplementation -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "fullname" -}}
{{- $name := default .Chart.Name (include "hname" .) -}}
{{- printf "%s" $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{/*

{{/*
Renders the image spec. Pass the <component>.<image> mapping as context to this function
*/}}
{{- define "imagespec" -}}
{{- printf "%s/%s:%s" .repo .name .tag -}}
{{- end -}}
