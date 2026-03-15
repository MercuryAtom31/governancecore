import "./FormField.css";

// This is a Form field primitive UI which will be reused throughout the project.

type Props = {
  label: string;
  children: React.ReactNode;
};

export default function FormField({ label, children }: Props) {
  return (
    <div className="ui-form-field">
      <label className="ui-form-label">{label}</label>
      {children}
    </div>
  );
}